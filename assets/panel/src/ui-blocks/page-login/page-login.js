import React, {Component} from 'react';
import './page-login.css';

// UI-Blocks
import LayoutLogin from './../layout-login/layout-login';
import Form from './../form/form';
import FormField, {FormFieldTypes} from './../form-field/form-field';


class PageLogin extends Component {

    render() {
        return (
            <LayoutLogin>
                <Form>
                    <div className="form-block form__title">
                        Войти в аккаунт
                    </div>

                    <div className="form-block">
                        <FormField
                            type={FormFieldTypes.INPUT}
                            placeholder="Имя пользователя"/>

                        <FormField
                            type={FormFieldTypes.INPUT}
                            placeholder="Пароль"
                            password/>
                    </div>

                    <div className="form-block">
                        <FormField type={FormFieldTypes.BUTTON}/>
                    </div>

                    <div className="form-block">
                        <FormField type={FormFieldTypes.BUTTON}/>
                    </div>
                </Form>
            </LayoutLogin>
        )
    }

}

export default PageLogin;
