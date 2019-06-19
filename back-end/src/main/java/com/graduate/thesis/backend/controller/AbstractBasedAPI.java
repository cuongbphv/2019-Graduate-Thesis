package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Huy Pham
 */

public abstract class AbstractBasedAPI {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected ResponseUtil responseUtil;

}
