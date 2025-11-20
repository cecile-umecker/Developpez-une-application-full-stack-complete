import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Custom validator function for password strength validation.
 * 
 * This validator ensures that passwords meet security requirements by checking for:
 * - At least 8 characters in length
 * - At least one uppercase letter (A-Z)
 * - At least one lowercase letter (a-z)
 * - At least one digit (0-9)
 * - At least one special character (!@#$%^&*(),.?":{}|<>)
 * 
 * @param control - The form control containing the password value to validate
 * @returns ValidationErrors object with 'required' if empty, 'passwordStrength' if invalid, or null if valid
 * 
 * @example
 * ```typescript
 * this.passwordControl = new FormControl('', [passwordStrengthValidator]);
 * ```
 */
export const passwordStrengthValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const value = control.value;
  if (!value) return { required: true };

  const hasUpperCase = /[A-Z]/.test(value);
  const hasLowerCase = /[a-z]/.test(value);
  const hasNumber = /\d/.test(value);
  const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(value);
  const minLength = value.length >= 8;

  const valid = hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar && minLength;

  return valid ? null : { passwordStrength: true };
};
