import React, {Component} from 'react';
import './page-settings.css';

// UI-Blocks
import LayoutMain from '../layout-main/layout-main';
import FormField, {FormFieldTypes, FormFieldModes} from '../form-field/form-field';
import Delimiter from '../delimiter/delimiter';

// Form
import Form from '../../form/form';

// Utils
import Validators from '../../utils/validators/validators';


export default class PageSettings extends Component {

    emailForm = new Form();
    passwordForm = new Form();
    avatarForm = new Form();

    constructor(props) {
        super(props);
        this.state = {emailValidated: false};

        this.onEmailSubmit = this.onEmailSubmit.bind(this);
        this.onEmailFocus = this.onEmailFocus.bind(this);
        this.onEmailBlur = this.onEmailBlur.bind(this);
    }

    onEmailSubmit() {
        this.setState({emailValidated: true});
        if (!this.emailForm.validate()) return;

        alert('ok!');
    }

    onEmailFocus() {
        this.emailForm.toggleTooltip(false);
    }

    onEmailBlur() {
        if (this.state.emailValidated) {
            this.emailForm.validate();
        }
    }

    render() {
        return (
            <LayoutMain title="Настройки">
                <Delimiter text="Смена адреса электронной почты"/>

                <div className="big-form">
                    <div className="big-form__field">
                        <span className="big-form__field__title">Email:</span>
                        <FormField
                            ref={this.emailForm.add('email')}
                            type={FormFieldTypes.INPUT}
                            placeholder="Email"
                            mode={FormFieldModes.FLEXIBLE}
                            validator={Validators.email}
                            onFocus={this.onEmailFocus}
                            onBlur={this.onEmailBlur}/>
                    </div>

                    <div className="big-form__field">
                        <span className="big-form__field__title"/>
                        <FormField
                            type={FormFieldTypes.BUTTON}
                            mode={FormFieldModes.FLEXIBLE}
                            onAction={this.onEmailSubmit}
                            text="Поменять"/>
                    </div>
                </div>

                <Delimiter text="Смена пароля"/>

                <div className="big-form">
                    <div className="big-form__field">
                        <span className="big-form__field__title">Старый пароль:</span>
                        <FormField
                            ref={this.passwordForm.add('old_password')}
                            type={FormFieldTypes.INPUT}
                            placeholder="Старый пароль"
                            password
                            mode={FormFieldModes.FLEXIBLE}
                            onFocus={this.onFocus}
                            onBlur={this.onBlur}/>
                    </div>

                    <div className="big-form__field">
                        <span className="big-form__field__title">Новый пароль:</span>
                        <FormField
                            ref={this.passwordForm.add('new_password')}
                            type={FormFieldTypes.INPUT}
                            placeholder="Новый пароль"
                            password
                            mode={FormFieldModes.FLEXIBLE}
                            onFocus={this.onFocus}
                            onBlur={this.onBlur}/>
                    </div>

                    <div className="big-form__field">
                        <span className="big-form__field__title"/>
                        <FormField
                            type={FormFieldTypes.BUTTON}
                            mode={FormFieldModes.FLEXIBLE}
                            text="Поменять"/>
                    </div>
                </div>

                <Delimiter text="Смена аватара"/>
                <div className="big-form">
                    <div className="big-form__field">
                        <span className="big-form__field__title"/>
                        <FormField
                            type={FormFieldTypes.BUTTON}
                            mode={FormFieldModes.FLEXIBLE}
                            text="Поменять"/>
                    </div>
                </div>

                <Delimiter text="Активные сессии"/>

                <Delimiter text="История входов"/>
            </LayoutMain>
        );
    }

}
