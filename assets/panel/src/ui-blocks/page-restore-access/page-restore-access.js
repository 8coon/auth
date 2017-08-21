import React, {Component} from 'react';
import PropTypes from 'prop-types';
import './page-restore-access.css';

// UI-Blocks
import LayoutLogin from './../layout-login/layout-login';
import FormField, {FormFieldTypes} from './../form-field/form-field';
import LayerNotify from './../layer-notify/layer-notify';

// Form
import Form from '../../form/form';

// Requests
import restoreAccess from 'minecraftshire-jsapi/src/method/auth/restoreAccess';

// Sitemap
import Sitemap from '../../sitemap';

// Utils
import Validators from '../../utils/validators/validators';


export default class PageRestoreAccess extends Component {

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

        const email = this.form.fields.email.getText();

        restoreAccess(email)
            .then(() => this.context.router.history.push(Sitemap.restoreSuccess))
            .catch(xhr => {
                if (!xhr || !xhr.body || xhr.body.error !== 'failed') {
                    LayerNotify.addNotify({text: 'Что-то пошло не так!'});
                    return;
                }

                const email = this.form.fields.email;

                switch (xhr.body.cause) {
                    case 'not found':
                        email.validate('Аккаунт с таким Email не найден!');
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
                        Восстановить доступ
                    </div>

                    <div className="form-block form__desc">
                        Укажите Email, на который вы регистрировали свой аккаунт:
                    </div>

                    <div className="form-block">
                        <FormField
                            ref={this.form.add('email')}
                            type={FormFieldTypes.INPUT}
                            placeholder="Email"
                            validator={Validators.email}
                            onFocus={this.onFocus}
                            onBlur={this.onBlur}/>

                        <FormField
                            type={FormFieldTypes.BUTTON}
                            text="Восстановить доступ"
                            onAction={this.onSubmit}/>
                    </div>

                    <div className="form-block form__links">
                    </div>
                </div>
            </LayoutLogin>
        )
    }

}
