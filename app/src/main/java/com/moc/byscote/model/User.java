package com.moc.byscote.model;

import java.io.Serializable;

/**
 * Created by TayMyanmar on 07/12/2016.
 */
public class User implements Serializable {
    public String id = "empty";
    public String first_name = "empty";
    public String last_name = "empty";
    public String email = "empty";
    public AgeRange age_range = new AgeRange();
    public String gender = "empty";
}
