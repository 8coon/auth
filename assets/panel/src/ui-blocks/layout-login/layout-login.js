import React, {Component} from 'react';
import './layout-login.css';
import Logo from '../../assets/logo.png';


class LayoutLogin extends Component {

    render() {
        return (
            <div className="layout-login">
                <div className="layout-login__outer">
                    <div className="layout-login__inner">
                        <div className="layout-login__logo" style={{backgroundImage: `url(${Logo})`}}/>
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

