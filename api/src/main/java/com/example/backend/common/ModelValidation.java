package com.example.backend.common;

public class ModelValidation {
    public static final String INVALID = "Không hợp lệ";
    public static final String NOT_NULL = "Giá trị tham số không được null";
    public static final String NOT_EMPTY = "Giá trị tham số không được null hoặc để trống";
    public static final String POSITIVE_OR_ZERO = "Giá trị tham số phải là một số dương";
    public static final String POSITIVE = "Giá trị tham số phải là một số lớn hơn không";
    public static final String YYYYMMDD_INVALID = "Không hợp lệ theo định dạng yyyy-MM-dd";
    public static final String YYYYMMDDHHMMSS_INVALID = "Không hợp lệ theo định dạng yyyy-MM-dd HH:mm:ss";
    public static final String MIN = "Giá trị tham số không được nhỏ hơn";
    public static final String MAX = "Giá trị tham số không được lớn hơn";
    public static final String LENGTH_MIN = "Độ dài tối thiểu của tham số không hợp lệ";
    public static final String LENGTH_MAX = "Độ dài tối đa của tham số không hợp lệ";
}
