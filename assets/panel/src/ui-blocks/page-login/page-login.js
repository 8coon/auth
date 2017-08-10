import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import './page-login.css';

// UI-Blocks
import LayoutLogin from './../layout-login/layout-login';
import FormField, {FormFieldTypes} from './../form-field/form-field';
import LayerNotify from './../layer-notify/layer-notify';

// Sitemap
import Sitemap from '../../sitemap';

// Form
import Form from '../../form/form';

// Services
import Storage from '../../services/storage';

// Requests
import loginUser from 'minecraftshire-jsapi/src/method/auth/login';


export default class PageLogin extends Component {

    form = new Form();

    constructor(props) {
        super(props);
        this.state = {validated: false};

        this.onSubmit = this.onSubmit.bind(this);
        this.onFocus = this.onFocus.bind(this);
        this.onBlur = this.onBlur.bind(this);
    }

    onFocus() {
        this.form.toggleTooltip(false);
    }

    onBlur() {
        if (this.state.validated) {
            this.form.validate();
        }
    }

    onSubmit() {
        this.setState({validated: true});
        if (!this.form.validate()) return;

        const username = this.form.fields.username.getText();
        const password = this.form.fields.password.getText();

        loginUser(username, password)
            .then(res => {
               Storage.setUsername(username);
               Storage.set('authToken', res.authToken);

               alert('ok!');
            })
            .catch(xhr => {
                if (!xhr || !xhr.body || xhr.body.error !== 'wrong credentials') {
                    LayerNotify.addNotify({text: 'Что-то пошло не так!'});
                    return;
                }

                const password = this.form.fields.password;

                switch (xhr.body.cause) {
                    case 'username or password':
                        password.validate('Неправильный логин или пароль!');
                        password.toggleTooltip(true);
                        break;

                    default: break;
                }
            });
    }

    render() {
        return (
            <LayoutLogin>
                <div className="form">
                    <div className="form-block form__title">
                        Войти в аккаунт
                    </div>

                    <div className="form-block">
                        <FormField
                            ref={this.form.add('username')}
                            type={FormFieldTypes.INPUT}
                            placeholder="Имя пользователя"
                            validator={PageLogin.validateUsername}
                            onFocus={this.onFocus}
                            onBlur={this.onBlur}/>

                        <FormField
                            ref={this.form.add('password')}
                            type={FormFieldTypes.INPUT}
                            placeholder="Пароль"
                            password
                            validator={PageLogin.validatePassword}
                            onFocus={this.onFocus}
                            onBlur={this.onBlur}/>

                        <FormField
                            type={FormFieldTypes.BUTTON}
                            text="Войти"
                            onAction={this.onSubmit}/>
                    </div>

                    <div className="form-block form__links">
                        <div>
                            Не получается войти в аккаунт?&nbsp;
                            <Link to={Sitemap.restoreAccess}>Восстановить пароль</Link>
                        </div>
                        <div>
                            Нет аккаунта?&nbsp;
                            <Link to={Sitemap.signup}>Зарегистрироваться</Link>
                        </div>
                    </div>
                </div>
            </LayoutLogin>
        )
    }

}
