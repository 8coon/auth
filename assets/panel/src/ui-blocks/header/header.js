import React, {Component} from 'react';
import PropTypes from 'prop-types';
import './header.css';

// Assets
import Logo from '../../assets/logo.png';
import NoAvatar from '../../assets/no-avatar.jpg';

// Sitemap
import Sitemap from '../../sitemap';

// Size
import {LayoutSize} from '../layout/layout';

// UI-Blocks
import Dropdown from '../dropdown/dropdown';


export default class Header extends Component {

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
            <div className={`header header_size_${this.props.size}`}>
                {
                    this.props.size === LayoutSize.handheld &&
                        <div className="header__wrapper">
                            <div className="header__burger"
                                 onClick={this.onBurgerClick}>
                                <i className="fa fa-bars" aria-hidden="true"/>
                            </div>

                            <div className="header__account-mobile">
                                <Dropdown controlText="Dropdown" transparent>
                                    <div className="dropdown__item">
                                        Item 1
                                    </div>
                                    <div className="dropdown__item">
                                        Item 2
                                    </div>
                                    <div className="dropdown__item">
                                        Item 3
                                    </div>
                                </Dropdown>
                            </div>
                        </div>
                }
                {
                    this.props.size !== LayoutSize.handheld &&
                    <div className="header__wrapper">
                        <div className="header__logo"
                             style={{backgroundImage: `url(${Logo})`}}
                             onClick={this.onLogoClick}/>

                        <div className="header__search">
                            Search
                        </div>

                        <div className="header__account">
                            Account
                        </div>
                    </div>
                }
            </div>
        )
    }

}
