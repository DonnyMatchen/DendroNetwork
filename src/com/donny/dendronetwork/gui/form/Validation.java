package com.donny.dendronetwork.gui.form;

import com.donny.dendronetwork.gui.customswing.DendroFactory;
import com.donny.dendronetwork.json.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Validation {
    public static void require(JTextField field) throws ValidationFailedException {
        field.setText(field.getText().replace("\"", ""));
        if (field.getText().equals("")) {
            field.setBackground(DendroFactory.WRONG);
            throw new ValidationFailedException("Field cannot be empty");
        } else {
            field.setBackground(DendroFactory.CONTENT);
        }
    }

    public static void require(JTextArea field) throws ValidationFailedException {
        field.setText(field.getText().replace("\"", ""));
        if (field.getText().equals("")) {
            field.setBackground(DendroFactory.WRONG);
            throw new ValidationFailedException("Field cannot be empty");
        } else {
            field.setBackground(DendroFactory.CONTENT);
        }
    }

    public static BigDecimal validateDecimal(JTextField field) throws ValidationFailedException {
        require(field);
        try {
            field.setBackground(DendroFactory.CONTENT);
            return new BigDecimal(field.getText());
        } catch (NumberFormatException e) {
            field.setBackground(DendroFactory.WRONG);
            throw new ValidationFailedException("Field is not a valid number");
        }
    }

    public static BigInteger validateInteger(JTextField field) throws ValidationFailedException {
        require(field);
        try {
            field.setBackground(DendroFactory.CONTENT);
            return new BigInteger(field.getText());
        } catch (NumberFormatException e) {
            field.setBackground(DendroFactory.WRONG);
            throw new ValidationFailedException("Field is not a valid integer");
        }
    }

    public static String validateString(JTextField field) throws ValidationFailedException {
        require(field);
        field.setBackground(DendroFactory.CONTENT);
        return field.getText();
    }

    public static String validateStringAllowEmpty(JTextField field) {
        field.setText(field.getText().replace("\"", ""));
        field.setBackground(DendroFactory.CONTENT);
        return field.getText();
    }

    public static String validateString(JTextArea field) throws ValidationFailedException {
        require(field);
        field.setBackground(DendroFactory.CONTENT);
        return field.getText();
    }

    public static String validateStringAllowEmpty(JTextArea field) {
        field.setText(field.getText().replace("\"", ""));
        field.setBackground(DendroFactory.CONTENT);
        return field.getText();
    }

    public static JsonItem validateJson(JTextArea field) throws ValidationFailedException {
        require(field);
        try {
            field.setBackground(DendroFactory.CONTENT);
            return JsonItem.sanitizeDigest(field.getText());
        } catch (JsonFormattingException e) {
            field.setBackground(DendroFactory.WRONG);
            throw new ValidationFailedException("Field is not a valid JSON");
        }
    }

    public static JsonObject validateJsonObject(JTextArea field) throws ValidationFailedException {
        JsonItem item = validateJson(field);
        if (item.getType() == JsonType.OBJECT) {
            field.setBackground(DendroFactory.CONTENT);
            return (JsonObject) item;
        } else {
            field.setBackground(DendroFactory.WRONG);
            throw new ValidationFailedException("Field is not a valid JSON Object");
        }
    }

    public static JsonArray validateJsonArray(JTextArea field) throws ValidationFailedException {
        JsonItem item = validateJson(field);
        if (item.getType() == JsonType.ARRAY) {
            field.setBackground(DendroFactory.CONTENT);
            return (JsonArray) item;
        } else {
            field.setBackground(DendroFactory.WRONG);
            throw new ValidationFailedException("Field is not a valid JSON Array");
        }
    }
}
