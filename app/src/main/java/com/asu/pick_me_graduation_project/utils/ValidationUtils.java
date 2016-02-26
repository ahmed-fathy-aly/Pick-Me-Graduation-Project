package com.asu.pick_me_graduation_project.utils;

/**
 * Created by ahmed on 2/22/2016.
 */
public class ValidationUtils
{
    /**
     * check it's a non null, non empty string
     */
    public static boolean notEmpty(String str)
    {
        if (str == null)
            return  false;
        if (str.length() == 0)
            return false;
        return true;
    }
}
