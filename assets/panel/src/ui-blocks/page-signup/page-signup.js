import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import './page-signup.css';

// UI-Blocks
import LayoutLogin from './../layout-login/layout-login';
import FormField, {FormFieldTypes} from './../form-field/form-field';
import LayerNotify from './../layer-notify/layer-notify';

// Form
import Form from '../../form/form';

// Requests
import createUser from 'minecraftshire-jsapi/src/method/user/create';

// Sitemap
import Sitemap from '../../sitemap';

// Utils
import Validators from '../../utils/validators/validators';


export default class PageSignup extends Component {

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
        const email = this.form.fields.email.getText();
        const password = this.form.fields.password.getText();

        createUser(username, password, email)
            .then(() => alert('ok!'))
            .catch(xhr => {
                if (!xhr || !xhr.body || xhr.body.error !== 'exists') {
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
                            validator={Validators.username}
                            onFocus={this.onFocus}
                            onBlur={this.onBlur}/>

                        <FormField
                            ref={this.form.add('email')}
                            type={FormFieldTypes.INPUT}
                            placeholder="Email"
                            validator={Validators.email}
                            onFocus={this.onFocus}
                            onBlur={this.onBlur}/>

                        <FormField
                            ref={this.form.add('password')}
                            type={FormFieldTypes.INPUT}
                            placeholder="Пароль"
                            password
                            validator={Validators.password}
                            onFocus={this.onFocus}
                            onBlur={this.onBlur}/>

                        <FormField
                            type={FormFieldTypes.BUTTON}
                            text="Создать аккаунт"
                            onAction={this.onSubmit}/>
                    </div>

                    <div className="form-block form__links">
                        <div>
                            Уже есть аккаунт?&nbsp;
                            <Link to={Sitemap.login}>Войти</Link>
                        </div>
                    </div>
                </div>
            </LayoutLogin>
        )
    }

}
