import React, {Component} from 'react';
import PropTypes from 'prop-types';
import './page-notifications.css';

// UI-Blocks
import LayoutMain from '../layout-main/layout-main';

// Requests
import listNotifications from 'minecraftshire-jsapi/src/method/notification/list';


export default class PageNotifications extends Component {

    static contextTypes = {
        router: PropTypes.object,
        model: PropTypes.object,
    };

    static prepare() {
        return listNotifications().then((notifications) => {
            return {allNotifications: notifications};
        });
    }

    render() {
        console.log(this.context.model.allNotifications);

        return (
            <LayoutMain title="Уведомления">
            </LayoutMain>
        )
    }

}
