package com.example.android.bakingapp.util;

public final class ApiResponse<T> {
    public final boolean isSuccessful;
    public final T data;
    public final String errorMessage;

    private ApiResponse(boolean isSuccessful, T data, String errorMessage) {
        this.isSuccessful = isSuccessful;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> failure(String errorMessage) {
        return new ApiResponse<>(false, null, errorMessage);
    }
}