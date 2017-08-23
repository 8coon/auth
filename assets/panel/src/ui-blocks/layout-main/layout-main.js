import React, {Component} from 'react';
import PropTypes from 'prop-types';
import './layout-main.css';
import Logo from '../../assets/logo.png';

// Sitemap
import Sitemap from '../../sitemap';

// UI-Blocks
import Layout from '../layout/layout';
import LayerNotify from '../layer-notify/layer-notify';
import LayerPopup from '../layer-popup/layer-popup';
import Header from '../header/header';
import Menu from '../menu/menu';


export default class LayoutMain extends Component {

    static contextTypes = {
        router: PropTypes.object,
    };

    static defaultProps = {
        title: '',
    };

    constructor(props) {
        super(props);

        this.onBurgerClick = this.onBurgerClick.bind(this);
    }

    onBurgerClick() {
        this.refs.menu.toggle();
    }

    render() {
        return (
            <div className="layout-main">
                <Layout style={{boxSizing: 'border-box'}}>
                    <Header onBurgerClick={this.onBurgerClick}/>
                    <Menu ref="menu" title={this.props.title}>
                        {this.props.children}
                    </Menu>
                </Layout>

                <LayerNotify/>
                <LayerPopup/>
            </div>
        )
    }

}
