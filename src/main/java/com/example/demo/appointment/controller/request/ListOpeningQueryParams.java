package com.example.demo.appointment.controller.request;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;

public class ListOpeningQueryParams {
    /**
     * From date query parameter for filter the open slots
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;

    /**
     * To date query parameter for filter the open slots
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate toDate;

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    @AssertTrue(message = "Values are invalid")
    public boolean isValid() {
        return fromDate != null && toDate != null
                && (toDate.isAfter(fromDate) || toDate.isEqual(fromDate));
    }
}
