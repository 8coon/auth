import React, {Component} from 'react';
import PropTypes from 'prop-types';
import './layout-login.css';
import Logo from '../../assets/logo.png';

// Sitemap
import Sitemap from '../../sitemap';

// UI-Blocks
import LayerNotify from '../layer-notify/layer-notify';
import LayerPopup from '../layer-popup/layer-popup';


export default class LayoutLogin extends Component {

    static contextTypes = {
        router: PropTypes.object,
    };

    constructor(props) {
        super(props);

        this.onLogoClick = this.onLogoClick.bind(this);
    }

    onLogoClick() {
        this.context.router.history.push(Sitemap.root);
    }

    render() {
        return (
            <div className="layout-login">
                <div className="layout-login__outer">
                    <div className="layout-login__inner">
                        <div
                            className="layout-login__logo"
                            style={{backgroundImage: `url(${Logo})`}}
                            onClick={this.onLogoClick}/>

                        <div className="layout-login__content">
                            {this.props.children}
                        </div>
                    </div>
                </div>

                <LayerNotify/>
                <LayerPopup/>
            </div>
        )
    }

}

