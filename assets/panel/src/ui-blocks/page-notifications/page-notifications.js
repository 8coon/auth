import React, {Component} from 'react';
import PropTypes from 'prop-types';
import './page-notifications.css';

// UI-Blocks
import LayoutMain from '../layout-main/layout-main';


export default class PageNotifications extends Component {

    static contextTypes = {
        router: PropTypes.object,
        model: PropTypes.object,
    };

    static prepare() {
        return new Promise(resolve => {
            window.setTimeout(() => resolve({text: 'Mae Borowski'}), 1000);
        });
    }

    render() {
        return (
            <LayoutMain title="Уведомления">
                {this.context.model.text}
            </LayoutMain>
        )
    }

}
