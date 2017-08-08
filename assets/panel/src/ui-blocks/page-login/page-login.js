import React, {Component} from 'react';
import './page-login.css';
import Logo from './../../assets/logo.png';

// UI-Blocks
import LayoutLogin from './../layout-login/layout-login';
import Form from './../form/form';
import FormField, {FormFieldTypes} from './../form-field/form-field';


class PageLogin extends Component {

    render() {
        return (
            <LayoutLogin>
                <Form>
                    <img src={Logo} alt="logo"/>

                    <FormField type={FormFieldTypes.INPUT}/>
                    <FormField type={FormFieldTypes.BUTTON}/>
                </Form>
            </LayoutLogin>
        )
    }

}

export default PageLogin;
