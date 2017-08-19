import React, {Component} from 'react';
import PropTypes from 'prop-types';
import './page-notifications.css';
import NoAvatar from '../../assets/no-avatar.jpg';

// Utils
import {dateDiffInDays, formatDate} from '../../utils/polyfills';

// UI-Blocks
import LayoutMain from '../layout-main/layout-main';
import Delimiter from '../delimiter/delimiter';
import Avatar from '../avatar/avatar';

// Requests
import listNotifications from 'minecraftshire-jsapi/src/method/notification/list';
import markReadNotifications from 'minecraftshire-jsapi/src/method/notification/markRead';

// Services
import Status from '../../services/status';


export default class PageNotifications extends Component {

    static contextTypes = {
        router: PropTypes.object,
        model: PropTypes.object,
    };

    static instances = [];

    static prepare() {
        return new Promise(resolve => {
            listNotifications()
                .then((notifications) => {
                    resolve({error: null, allNotifications: notifications});

                    const ids = notifications
                        .filter(notification => notification.is('unread'))
                        .pluck('id');

                    if (ids.length > 0) {
                        markReadNotifications(ids)
                            .then(() => PageNotifications.refresh())
                            .then(() => Status.reloadModel(true));
                    }
                })
                .catch(() => {
                    resolve({error: 'Не удалось загрузить данные.'});
                });
        });
    }

    static refresh() {
        return PageNotifications.prepare()
            .then(model => PageNotifications.refreshInstances(model));
    }

    static refreshInstances(model) {
        PageNotifications.instances.forEach(instance => {
            Object.assign(instance.context.model, model);
            instance.setState({});
        });
    }

    constructor(props) {
        super(props);
        this.state = {allRead: false};

        this.onStatusChange = this.onStatusChange.bind(this);
    }

    componentDidMount() {
        PageNotifications.instances.push(this);
        Status.$on(Status.EVT_STATUS_CHANGE, this.onStatusChange);
    }

    componentWillUnmount() {
        PageNotifications.instances.splice(PageNotifications.instances.indexOf(this), 1);
        Status.$off(Status.EVT_STATUS_CHANGE, this.onStatusChange);
    }

    onStatusChange(evt) {
        PageNotifications.refresh();
    }

    renderNotifications(blocks) {
        const result = [];
        const now = new Date(Date.now());

        blocks.forEach(block => {
            if (block.length === 0) {
                return;
            }

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
            <Delimiter key={key} text={diffText}/>
        );
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
                <Avatar url={notification.get('pictureUrl')}/>
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
