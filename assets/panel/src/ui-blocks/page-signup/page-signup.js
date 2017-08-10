import React, {Component} from 'react';
import './page-signup.css';

// UI-Blocks
import LayoutLogin from './../layout-login/layout-login';
import FormField, {FormFieldTypes} from './../form-field/form-field';
import LayerNotify from './../layer-notify/layer-notify';

// Form
import Form from '../../form/form';

// Requests
import createUser from 'minecraftshire-jsapi/src/method/user/create';


export default class PageSignup extends Component {

    form = new Form();

    constructor(props) {
        super(props);
        this.state = {validated: false};

        PageSignup.validateUsername = PageSignup.validateUsername.bind(this);
        PageSignup.validateEmail = PageSignup.validateEmail.bind(this);
        PageSignup.validatePassword = PageSignup.validatePassword.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.onFocus = this.onFocus.bind(this);
        this.onBlur = this.onBlur.bind(this);
    }

    static validateUsername(value) {
        if (value.length < 4) {
            return `Минимальная длина — 4 символа!`;
        }

        if (value.length > 30) {
            return `Максимальная длина — 30 символов!`
        }

        if (!/[a-zA-Z0-9._=[\]]+/.test(value)) {
            return 'Разрешены только символы: a-z A-Z 0-9 ._=[]';
        }

        return true;
    }

    static validatePassword(value) {
        if (value.length < 4) {
            return `Минимальная длина — 4 символа!`;
        }

        if (value.length > 30) {
            return `Максимальная длина — 30 символов!`
        }

        return true;
    }

    static validateEmail(value) {
        if (!/.*@.*/.test(value)) {
            return 'Неверный формат Email-адреса!';
        }

        return true;
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

        if (!this.form.validate()) {
            return;
        }

        const username = this.form.fields.username.getText();
        const email = this.form.fields.email.getText();
        const password = this.form.fields.password.getText();

        createUser(username, password, email)
            .then(() => alert('ok!'))
            .catch(xhr => {
                if (xhr.body.error !== 'exists') {
                    LayerNotify.addNotify({text: 'Что-то пошло не так!'});
                    return;
                }

                const username = this.form.fields.username;
                const email = this.form.fields.email;

                switch (xhr.body.cause) {
                    case 'username':
                        username.validate('Такой пользователь уже существует!');
                        username.toggleTooltip(true);
                        break;

                    case 'email':
                        email.validate('Такой Email уже используется!');
                        email.toggleTooltip(true);
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
                        Создать аккаунт
                    </div>

                    <div className="form-block">
                        <FormField
                            ref={this.form.add('username')}
                            type={FormFieldTypes.INPUT}
                            placeholder="Имя пользователя"
                            validator={PageSignup.validateUsername}
                            onFocus={this.onFocus}
                            onBlur={this.onBlur}/>

                        <FormField
                            ref={this.form.add('email')}
                            type={FormFieldTypes.INPUT}
                            placeholder="Email"
                            validator={PageSignup.validateEmail}
                            onFocus={this.onFocus}
                            onBlur={this.onBlur}/>

                        <FormField
                            ref={this.form.add('password')}
                            type={FormFieldTypes.INPUT}
                            placeholder="Пароль"
                            password
                            validator={PageSignup.validatePassword}
                            onFocus={this.onFocus}
                            onBlur={this.onBlur}/>

                        <FormField
                            type={FormFieldTypes.BUTTON}
                            text="Создать аккаунт"
                            onAction={this.onSubmit}/>
                    </div>

                    <div className="form-block"/>
                </div>
            </LayoutLogin>
        )
    }

}
