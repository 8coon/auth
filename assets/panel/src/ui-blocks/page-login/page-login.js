import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import './page-login.css';

// Sitemap
import Sitemap from '../../sitemap';

// UI-Blocks
import LayoutLogin from './../layout-login/layout-login';
import Form from '../../form/form';
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

                        <FormField
                            type={FormFieldTypes.BUTTON}
                            text="Войти"/>
                    </div>

                    <div className="form-block form__links">
                        <div>
                            Не получается войти в аккаунт?&nbsp;
                            <Link to={Sitemap.login}>Восстановить пароль</Link>
                        </div>
                        <div>
                            Нет аккаунта?&nbsp;
                            <Link to={Sitemap.signup}>Зарегистрироваться</Link>
                        </div>
                    </div>
                </Form>
            </LayoutLogin>
        )
    }

}

export default PageLogin;
