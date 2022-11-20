package com.openlap.dataset;

/**
 * This object encapsulates the validation operation of a configuration of an OpenLAPDataSet. It contains two main fields:
 * One describing the validation result as a boolean, another with additional information of the validation process,
 * most useful when the validation does not pass, since describes which fields and reasons caused the validation
 * to fail.
 */
public class OpenLAPDataSetConfigValidationResult {
    public static final String VALID_CONFIGURATION = "Valid configuration";
    boolean valid;
    String validationMessage;

    /**
     * Empty constructor.
     */
    public OpenLAPDataSetConfigValidationResult() {
        this.valid = false;
        this.validationMessage = "";
    }

    /**
     * Constructor with validation setting and
     * @param isValid True if validation passed, false otherwise.
     * @param validationMessage Message for validation result.
     */
    public OpenLAPDataSetConfigValidationResult(boolean isValid, String validationMessage) {
        this.valid = isValid;
        this.validationMessage = validationMessage;
    }

    /**
     * @return The result of the validation.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @param valid Value for setting the result of the validation, true if valid, otherwise false.
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }


    /**
     * @return The information message of the validation.
     */
    public String getValidationMessage() {
        return validationMessage;
    }

    /**
     * @param validationMessage Message to be set as validation message.
     */
    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    /**
     * Appends a string to the validation message.
     * @param message Message to be appended to the validation message.
     * @return The new validation message.
     */
    public String appendValidationMessage(String message) {
      if(this.validationMessage.isEmpty() || this.validationMessage == null)
          this.validationMessage = message;
      else
          this.validationMessage = this.validationMessage + System.lineSeparator() + message;
      return this.validationMessage;
    }
}
