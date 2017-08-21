import React, {Component} from 'react';
import PropTypes from 'prop-types';
import './page-password-reset.css';

// UI-Blocks
import LayoutLogin from './../layout-login/layout-login';
import FormField, {FormFieldTypes} from './../form-field/form-field';
import LayerNotify from './../layer-notify/layer-notify';

// Form
import Form from '../../form/form';

// Requests
import restorePassword from 'minecraftshire-jsapi/src/method/auth/restorePassword';

// Sitemap
import Sitemap from '../../sitemap';

// Utils
import Validators from '../../utils/validators/validators';


export default class PagePasswordReset extends Component {

    form = new Form();

    static contextTypes = {
        router: PropTypes.object,
    };

    constructor(props) {
        super(props);
        this.state = {validated: false};

        this.onSubmit = this.onSubmit.bind(this);
        this.onFocus = this.onFocus.bind(this);
        this.onBlur = this.onBlur.bind(this);
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

        const location = this.context.router.route.location;
        const code = location.search.replace('?code=', '').replace('/', '');
        const password = this.form.fields.password.getText();

        restorePassword(code, password)
            .then(() => {
                this.context.router.history.push(Sitemap.login);

                window.setTimeout(() => {
                    LayerNotify.addNotify({text: 'Пароль успешно изменён!'});
                }, 1000);
            })
            .catch(xhr => LayerNotify.addNotify({text: 'Что-то пошло не так!'}));
    }

    render() {
        return (
            <LayoutLogin>
                <div className="form">
                    <div className="form-block form__title">
                        Сменить пароль
                    </div>

                    <div className="form-block form__desc">
                        Укажите новый пароль от аккаунта
                    </div>

                    <div className="form-block">
                        <FormField
                            ref={this.form.add('password')}
                            type={FormFieldTypes.INPUT}
                            password
                            placeholder="Пароль"
                            validator={Validators.password}
                            onFocus={this.onFocus}
                            onBlur={this.onBlur}/>

                        <FormField
                            type={FormFieldTypes.BUTTON}
                            text="Сменить пароль"
                            onAction={this.onSubmit}/>
                    </div>

                    <div className="form-block form__links">
                    </div>
                </div>
            </LayoutLogin>
        )
    }

}
