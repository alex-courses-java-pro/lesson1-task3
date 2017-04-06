package com.company;

import java.io.Serializable;

import javafx.beans.binding.ObjectExpression;

/**
 * Created by arahis on 4/5/17.
 */
public class SerializableField implements Serializable{
    private String name;
    private Class<?> type;
    private Object value;
}
