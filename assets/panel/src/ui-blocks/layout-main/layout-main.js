import React, {Component} from 'react';
import PropTypes from 'prop-types';
import './layout-main.css';
import Logo from '../../assets/logo.png';

// Sitemap
import Sitemap from '../../sitemap';

// UI-Blocks
import Layout from '../layout/layout';
import LayerNotify from '../layer-notify/layer-notify';
import Header from '../header/header';


export default class LayoutMain extends Component {

    static contextTypes = {
        router: PropTypes.object,
    };

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="layout-main">
                <Layout>
                    <Header/>
                </Layout>

            </div>
        )
    }

}
