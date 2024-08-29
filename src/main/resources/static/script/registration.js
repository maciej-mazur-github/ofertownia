$(document).ready(function () {
    'use strict'

    let forms = document.querySelectorAll(".needs-validation");

    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                registerValidationRules();
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }

                form.classList.add('was-validated');
            }, false);

            let emailExists = document.getElementById('email-exists');
            if (emailExists != null) {
                registerValidationRules();
                form.classList.add('was-validated');
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                }, false);
            }
        });
});

const registerValidationRules = function () {
    firstAndLastNameValidator();
    emailValidator();
    postalCodeValidator();
    passwordValidator();
}

const passwordValidator = () => {
    let passwordInput = document.getElementById('validationCustom09');
    validatePassword(passwordInput);
    passwordInput.addEventListener('input', function (event) {
        validatePassword(passwordInput);
    });
}

const validatePassword = (passwordInput) => {
    let errorMessage = '';
    let invalidMessageDiv = passwordInput.closest('.col-md').querySelector('.invalid-feedback');
    invalidMessageDiv.textContent = '';
    if (passwordInput.value.length === 0) {
        errorMessage = 'Pole nie może być puste';
        invalidMessageDiv.textContent = errorMessage;
        passwordInput.setCustomValidity('Invalid field'); // ustawia pole 'customError' na true
    }
    if (!checkPasswordRegexes(passwordInput, errorMessage, invalidMessageDiv)) {
        passwordInput.setCustomValidity('Invalid field'); // ustawia pole 'customError' na true
    } else {
        passwordInput.setCustomValidity('');
    }
}

const checkPasswordRegexes = (passwordInput, errorMessage, invalidMessageDiv) => {
    let atLeastOneLowerCaseLetterRegex = /.*[a-z].*/;
    let atLeastOneUpperCaseLetterRegex = /.*[A-Z].*/;
    let atLeastOneSpecialCharacterRegex = /.*[~!@#$%^&*()_+={\[}\]|"'<,>.?\/:-].*/;
    let atLeast8Characters = (password) => {
        return password.length >= 8;
    };

    let atLeastOneLowerCaseLetterRegexTestResult = atLeastOneLowerCaseLetterRegex.test(passwordInput.value);
    let atLeastOneUpperCaseLetterRegexTestResult = atLeastOneUpperCaseLetterRegex.test(passwordInput.value);
    let atLeastOneSpecialCharacterRegexTestResult = atLeastOneSpecialCharacterRegex.test(passwordInput.value);
    let atLeast8CharactersTestResult = atLeast8Characters(passwordInput.value);

    if (!atLeastOneLowerCaseLetterRegexTestResult) {
        let currentErrorMessage = invalidMessageDiv.textContent;
        if (currentErrorMessage.length > 0) {
            invalidMessageDiv.style.whiteSpace = 'pre-line';
            currentErrorMessage += '\n';
        }
        currentErrorMessage += 'Hasło musi zawierać minimum 1 znak mały';
        invalidMessageDiv.textContent = currentErrorMessage;
    }
    if (!atLeastOneUpperCaseLetterRegexTestResult) {
        let currentErrorMessage = invalidMessageDiv.textContent;
        if (currentErrorMessage.length > 0) {
            invalidMessageDiv.style.whiteSpace = 'pre-line';
            currentErrorMessage += '\n';
        }
        currentErrorMessage += 'Hasło musi zawierać minimum 1 znak duży';
        invalidMessageDiv.textContent = currentErrorMessage;
    }
    if (!atLeastOneSpecialCharacterRegexTestResult) {
        let currentErrorMessage = invalidMessageDiv.textContent;
        if (currentErrorMessage.length > 0) {
            invalidMessageDiv.style.whiteSpace = 'pre-line';
            currentErrorMessage += '\n';
        }
        currentErrorMessage += 'Hasło musi zawierać minimum 1 znak specjalny';
        invalidMessageDiv.textContent = currentErrorMessage;
    }
    if (!atLeast8CharactersTestResult) {
        let currentErrorMessage = invalidMessageDiv.textContent;
        if (currentErrorMessage.length > 0) {
            invalidMessageDiv.style.whiteSpace = 'pre-line';
            currentErrorMessage += '\n';
        }
        currentErrorMessage += 'Hasło musi mieć co najmniej 8 znaków';
        invalidMessageDiv.textContent = currentErrorMessage;
    }

    return atLeastOneLowerCaseLetterRegexTestResult
        && atLeastOneUpperCaseLetterRegexTestResult
        && atLeastOneSpecialCharacterRegexTestResult
        && atLeast8CharactersTestResult;
}

const postalCodeValidator = () => {
    let postalCodeInput = document.getElementById('validationCustom06');
    validatePostalCode(postalCodeInput);
    postalCodeInput.addEventListener('input', function (event) {
        validatePostalCode(postalCodeInput);
    });
}

const validatePostalCode = (postalCodeInput) => {
    let errorMessage = '';
    let invalidMessageDiv = postalCodeInput.closest('.col-md-2').querySelector('.invalid-feedback');
    if (postalCodeInput.value.length === 0) {
        errorMessage = errorMessage.concat(`Pole nie może być puste \n`);
        invalidMessageDiv.style.whiteSpace = 'pre-line';
        postalCodeInput.setCustomValidity('Invalid field'); // ustawia pole 'customError' na true
    }
    if (!checkPostalCodeRegex(postalCodeInput.value)) {
        errorMessage = errorMessage.concat('Kod pocztowy musi być zgodny z oficjalnym formatem');
        invalidMessageDiv.textContent = errorMessage;
        postalCodeInput.setCustomValidity('Invalid field'); // ustawia pole 'customError' na true
    } else {
        postalCodeInput.setCustomValidity('');
    }
}

const checkPostalCodeRegex = (postalCode) => {
    let postalCodeRegex = /[0-9]{2}-[0-9]{3}/;
    return postalCodeRegex.test(postalCode);
}

const emailValidator = () => {
    let emailInput = document.getElementById('validationCustom08');
    validateEmail(emailInput);
    emailInput.addEventListener('input', function (event) {
        validateEmail(emailInput);
    });
}

const validateEmail = (emailInput) => {
    let errorMessage = '';
    let invalidMessageDiv = emailInput.closest('.col-md').querySelector('.invalid-feedback');
    let existingEmailFromDataBaseDiv = document.getElementById('email-exists');

    if (emailInput.value.length === 0) {
        errorMessage = errorMessage.concat(`Pole nie może być puste \n`);
        invalidMessageDiv.style.whiteSpace = 'pre-line';
        emailInput.setCustomValidity('Invalid field');
    } else {
        emailInput.setCustomValidity('');
    }

    errorMessage = errorMessage.concat('Musi być poprawnie sformatowanym adresem email');
    invalidMessageDiv.textContent = errorMessage;

    if (existingEmailFromDataBaseDiv != null) {
        let existingEmailFromDataBase = $('meta[name="existing-email-from-db"]').attr('content');
        if (emailInput.value === existingEmailFromDataBase) {
            emailInput.setCustomValidity('Invalid value');
            existingEmailFromDataBaseDiv.textContent = 'Użytkownik o podanym adresie już istnieje w bazie';
            invalidMessageDiv.textContent = '';
        } else {
            existingEmailFromDataBaseDiv.textContent = '';
        }
    }
}

const firstAndLastNameValidator = () => {
    let firstNameInput = document.getElementById('validationCustom01');
    checkIfNotEmptyAndGreaterOrEqual3(firstNameInput);
    firstNameInput.addEventListener('input', function (event) {
        checkIfNotEmptyAndGreaterOrEqual3(firstNameInput);
    });

    let lastNameInput = document.getElementById('validationCustom02');
    checkIfNotEmptyAndGreaterOrEqual3(lastNameInput);
    lastNameInput.addEventListener('input', function (event) {
        checkIfNotEmptyAndGreaterOrEqual3(lastNameInput);
    });
}

const checkIfNotEmptyAndGreaterOrEqual3 = (element) => {
    let errorMessage = '';
    let invalidMessageDiv = element.closest('.col-md-6').querySelector('.invalid-feedback');
    if (element.value.length === 0) {
        errorMessage = errorMessage.concat(`Pole nie może być puste \n`);
        invalidMessageDiv.style.whiteSpace = 'pre-line';
    }
    if (element.value.length < 3) {
        errorMessage = errorMessage.concat('Pole musi mieć co najmniej 3 znaki');
        invalidMessageDiv.textContent = errorMessage;
        element.setCustomValidity('Invalid field');
    } else {
        element.setCustomValidity('');
    }
}
