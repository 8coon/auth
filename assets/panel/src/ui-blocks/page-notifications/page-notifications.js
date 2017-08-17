import React, {Component} from 'react';
import PropTypes from 'prop-types';
import './page-notifications.css';
import NoAvatar from '../../assets/no-avatar.jpg';

// Utils
import {dateDiffInDays, formatDate} from '../../utils/polyfills';

// UI-Blocks
import LayoutMain from '../layout-main/layout-main';

// Requests
import listNotifications from 'minecraftshire-jsapi/src/method/notification/list';
import markReadNotification from 'minecraftshire-jsapi/src/method/notification/markRead';


export default class PageNotifications extends Component {

    static contextTypes = {
        router: PropTypes.object,
        model: PropTypes.object,
    };

    static prepare() {
        return new Promise(resolve => {
            listNotifications()
                .then((notifications) => {
                    resolve({error: null, allNotifications: notifications});


                })
                .catch(() => {
                    resolve({error: 'Не удалось загрузить данные.'});
                });
        });
    }

    renderNotifications(blocks) {
        const result = [];
        const now = new Date(Date.now());

        blocks.forEach(block => {
            const days = dateDiffInDays(block[0].getCreatedAt(), now);

            if (days > 0) {
                result.push(this.renderDate(block[0].getCreatedAt(), days, result.length));
            }

            block.forEach(notification => {
                result.push(this.renderNotification(notification, result.length));
            });
        });

        return (<div className="notifications">{result}</div>)
    }

    renderDate(value, diff, key) {
        let diffText;

        switch(diff) {
            case 1: diffText ='Вчера'; break;
            case 2: diffText = 'Позавчера'; break;
            default: diffText = formatDate(value);
        }

        return (
            <div className="notification-delim" key={key}>
                <span className="notification-delim__text">
                    {diffText}
                </span>
            </div>
        )
    }

    renderNotification(notification, key) {
        return (
            <div className={`notification ${notification.unread ? 'notification_unread' : ''}`}
                 key={key}>
                <div className="notification__text">
                    <div className="notification__text__title">
                        {notification.get('title') || 'Уведомление'}
                    </div>
                    <div className="notification__text_desc">
                        {notification.get('text')}
                    </div>
                </div>
                <div className="notification__picture">
                    <div className="notification__picture__img"
                         style={{backgroundImage: `url(${notification.get('pictureUrl') || NoAvatar})`}}/>
                </div>
            </div>
        );
    }

    render() {
        const notifications = this.context.model.allNotifications;
        const error = this.context.model.error;
        const blocks = [];
        const incorrectBlock = [];
        let lastDate = new Date(Date.now());
        let currentBlock = [];

        if (!error) {
            notifications.forEach(notification => {
                if (notification.get('createdAt') === null) {
                    incorrectBlock.push(notification);
                    return;
                }

                const date = notification.getCreatedAt();

                if (dateDiffInDays(date, lastDate) > 0) {
                    lastDate = date;
                    blocks.push(currentBlock);
                    currentBlock = [];
                }

                currentBlock.push(notification);
            });

            if (currentBlock.length > 0) {
                blocks.push(currentBlock);
            }

            if (incorrectBlock.length > 0) {
                blocks.push(incorrectBlock);
            }
        }

        return (
            <LayoutMain title="Уведомления">
                {error && (
                    <div className="page__error">
                        {error}
                    </div>
                )}
                {!error && this.renderNotifications(blocks)}
            </LayoutMain>
        )
    }

}
