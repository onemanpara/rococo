import {Errors} from "$lib/types/Errors";
import {userFormErrorStore} from "$lib/components/forms/user/user-form.error.store";

export const validateForm = (
    firstname: string,
    lastname: string) => {

    userFormErrorStore.update((prevState) => {
        return {
            ...prevState,
            firstname: firstname?.length > 50
                ? Errors.NAME_LENGTH_CONSTRAINT_MAX
                : "",
            lastname: lastname?.length > 50
                ? Errors.SURNAME_LENGTH_CONSTRAINT_MAX
                : "",
        }
    });
}