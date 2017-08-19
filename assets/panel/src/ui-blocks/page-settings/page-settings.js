import React, {Component} from 'react';
import './page-settings.css';

// UI-Blocks
import LayoutMain from '../layout-main/layout-main';
import FormField, {FormFieldTypes, FormFieldModes} from '../form-field/form-field';
import Delimiter from '../delimiter/delimiter';

// Form
import Form from '../../form/form';


export default class PageSettings extends Component {

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

    }

    render() {
        return (
            <LayoutMain title="Настройки">
                <Delimiter text="Аккаунт"/>

                <div className="big-form">
                    <div className="big-form__field">
                        <span className="big-form__field__title">Email:</span>
                        <FormField
                            ref={this.form.add('email')}
                            type={FormFieldTypes.INPUT}
                            placeholder="Email"
                            mode={FormFieldModes.FLEXIBLE}
                            onFocus={this.onFocus}
                            onBlur={this.onBlur}/>
                    </div>

                    <div className="big-form__field">
                        <span className="big-form__field__title">Старый пароль:</span>
                        <FormField
                            ref={this.form.add('old_password')}
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
                            ref={this.form.add('new_password')}
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

                <Delimiter text="Профиль"/>

                <Delimiter text="Активные сессии"/>

                <Delimiter text="История входов"/>
            </LayoutMain>
        );
    }

}
