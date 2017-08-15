import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Link} from 'react-router-dom';
import './menu.css';

// Sitemap
import Sitemap from '../../sitemap';

// UI-Blocks
import Layout, {LayoutSize} from '../layout/layout';


export default class Menu extends Component {

    static contextTypes = {
        router: PropTypes.object,
    };

    constructor(props) {
        super(props);

        this.state = {expanded: false}
    }

    toggle(expanded) {
        this.setState({expanded: expanded === void 0 ? !this.state.expanded : expanded});
    }

    renderMenuItem(link, text, icon) {
        const location = this.context.router.route.match.path;
        const route = Sitemap[link];

        return (
            <Link to={route} title={text}
                  className={`menu__item ${location === route ? 'menu__item_selected' : ''}`}>
                <div className="menu__item__icon">
                    <i className={`fa ${icon}`} aria-hidden="true"/>
                </div>
                <div className="menu__item__text">{text}</div>
            </Link>
        )
    }

    renderLinks(expanded) {
        let absolute = false;
        let state = 'normal';

        if (this.props.size === LayoutSize.handheld) {
            if (expanded === void 0) {
                state = 'small';
            } else {
                state = expanded ? 'expanded' : 'collapsed';
                absolute = true;
            }
        }

        return (
            <div className={`scrollable menu__links menu__links_${state} ${absolute ? 'menu__links_absolute' : ''}`}>
                {this.renderMenuItem('root', 'Профиль', 'fa-user')}
                {this.renderMenuItem('root', 'Закладки', 'fa-star')}
                {this.renderMenuItem('root', 'Персонажи', 'fa-users')}
                {this.renderMenuItem('root', 'Государства', 'fa-flag')}
                {this.renderMenuItem('root', 'Финансы', 'fa-usd')}
                {this.renderMenuItem('root', 'Галерея', 'fa-camera-retro')}
                {this.renderMenuItem('root', 'Лаунчер', 'fa-rocket')}
                {this.renderMenuItem('root', 'Карта', 'fa-map')}
                {this.renderMenuItem('settings', 'Настройки', 'fa-cog')}
            </div>
        )
    }

    render() {
        return (
            <div className="menu">
                <div className="menu__strip"/>

                {this.renderLinks()}
                {this.props.size === LayoutSize.handheld && this.renderLinks(this.state.expanded)}

                <div className="menu__content">
                    Content
                </div>
            </div>
        );
    }

}
