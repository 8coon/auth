import React, {Component} from 'react';
import './layout-login.css';


class LayoutLogin extends Component {

    render() {
        return (
            <div className="layout-login">
                <div className="layout-login__outer">
                    <div className="layout-login__inner">
                        <div className="logo layout-login__logo"></div>
                        <div className="layout-login__content">
                            {this.props.children}
                        </div>
                    </div>
                </div>
            </div>
        )
    }

}


export default LayoutLogin;

