package demo.response.util;

import demo.response.APIResponse;

public class APIResponseUtil {

	public static String SUCCESS_STATUS = "200";
	public static String ERROR_STATUS = "500";
	public static String NOT_FOUND_STATUS = "404";
	public static String BAD_REQUEST_STATUS = "400";

	public static APIResponse successResponse(Object data) {
		APIResponse apiResponse = new APIResponse(SUCCESS_STATUS,"SUCCESS",data);
		return apiResponse;
	}
	
	public static APIResponse failResponse(Object data) {
		APIResponse apiResponse = new APIResponse(ERROR_STATUS,"ERROR",data);
		return apiResponse;
	}
	
	public static APIResponse notFoudResponse(Object data) {
		APIResponse apiResponse = new APIResponse(NOT_FOUND_STATUS,"NOT FOUND",data);
		return apiResponse;
	}
	
	public static APIResponse badResponse(Object data) {
		APIResponse apiResponse = new APIResponse(BAD_REQUEST_STATUS,"BAD REQUEST",data);
		return apiResponse;
	}
}
