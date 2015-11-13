package com.dr.frappe.api;

import android.util.Log;

import com.dr.frappe.model.ExpenseDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * Created by rohitman on 11/1/2015.
 */
public class ClientController {

    private String WHO_IS_LOGGING = ClientController.class.getName();

    private ObjectMapper objectMapper;
    private static ClientController instance;

    private static final String USER_ID_HEADER_KEY = "userId";
    private static final String API_BASE_URL = "http://frappe-1112.appspot.com/services/v1";
    private static final String GET_EXPENSES_URL = API_BASE_URL + "/expenses";
    private static final String SAVE_EXPENSE_URL = API_BASE_URL + "/expense";

    public synchronized static ClientController createInstance() {
        if (instance == null) {
            instance = new ClientController();
        }
        return instance;
    }

    public synchronized static ClientController getInstance() {
        if (instance == null) {
            throw new RuntimeException("Instance is not yet initialized");
        }
        return instance;
    }

    private void logErrorAndThrowException(String method, String url, HttpResponse response) {

        Log.e(WHO_IS_LOGGING, String.format("Error executing [%s] [%s] - [%d][%s]",
                method, url, response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase()));

        String errorMessage = "";
        try {
            errorMessage = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            Log.e(WHO_IS_LOGGING, "Error reading error response body", e);
        }

        Log.e(WHO_IS_LOGGING, String.format("Error response body [%s]", errorMessage));

        // TODO throw proper exception
        throw new RuntimeException();
    }

    private ClientController() {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibilityChecker(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<ExpenseDTO> getExpenses(String userId) {
        try {
            Log.i(WHO_IS_LOGGING, "get Expenses for UserID - " + userId);
            HttpClient client = new DefaultHttpClient();

            String url = String.format(GET_EXPENSES_URL);
            HttpUriRequest request = new HttpGet(url);
            request.addHeader(USER_ID_HEADER_KEY, userId);

            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                logErrorAndThrowException("GET", url, response);
            }

            HttpEntity httpEntity = response.getEntity();
            String apiOutput = EntityUtils.toString(httpEntity);

            List<ExpenseDTO> listExpenses =
                    objectMapper.readValue(apiOutput, new TypeReference<List<ExpenseDTO>>() {});
            Log.i(WHO_IS_LOGGING, "# Expenses returned: " + listExpenses.size());
            return listExpenses;

        } catch (Exception e) {
            Log.e(WHO_IS_LOGGING,  "Error getting all expenses for User", e);
            throw new RuntimeException(e);
        }
    }

    public void addExpense(ExpenseDTO expenseDTO, String userId) {
        try {
            Log.i(WHO_IS_LOGGING, "add Expense with head: " + expenseDTO.getExpenseHead() + " for user: " + userId);
            HttpClient client = new DefaultHttpClient();
            String url = String.format(SAVE_EXPENSE_URL);

            HttpPost request = new HttpPost(url);
            request.addHeader(USER_ID_HEADER_KEY, userId);

            String json = objectMapper.writeValueAsString(expenseDTO);
            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");

            request.setEntity(entity);

            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 204) {
                logErrorAndThrowException("POST", url, response);
            }

        } catch (Exception e) {
            Log.e(WHO_IS_LOGGING, "Error adding New Expense", e);
            throw new RuntimeException(e);
        }
    }
}