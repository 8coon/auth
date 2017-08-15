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
import Input from '../input/input';


export default class Header extends Component {

    static contextTypes = {
        router: PropTypes.object,
    };

    static defaultProps = {
        onBurgerClick: () => {},
    };

    constructor(props) {
        super(props);
        this.state = {search: false};

        this.onLogoClick = this.onLogoClick.bind(this);
        this.onSearchKeyPress = this.onSearchKeyPress.bind(this);
        this.onSearchFocus = this.onSearchFocus.bind(this);
        this.onSearchBlur = this.onSearchBlur.bind(this);
        this.onSearchClick = this.onSearchClick.bind(this);
    }

    onLogoClick() {
        this.context.router.history.push(Sitemap.root);
    }

    onSearchKeyPress(evt) {
        if (evt.keyCode === 13) {
            this.onSearchClick();
        }
    }

    onSearchFocus() {
        this.setState({search: true});
    }

    onSearchBlur() {
        this.setState({search: false});
        console.log('SEARCH UNFOCUS');
    }

    onSearchClick() {
        console.log('SEARCH CLICK');
    }

    render() {
        return (
            <div className={`header header_size_${this.props.size}`}>
                {
                    this.props.size === LayoutSize.handheld &&
                        <div className="header__wrapper">
                            <div className="header__burger"
                                 onClick={this.props.onBurgerClick}>
                                <i className="fa fa-bars" aria-hidden="true"/>
                            </div>

                            <div className="header__account-mobile">
                                <Dropdown controlText="Анатолий Ничведо"
                                          transparent>
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
                            <Input placeholder="Поиск..."
                                   onKeyPress={this.onSearchKeyPress}
                                   onFocus={this.onSearchFocus}
                                   onBlur={this.onSearchBlur}/>
                        </div>

                        <div className="header__account"
                             style={{opacity: this.state.search ? 0 : 1}}>
                            <div className="header__account__info">
                                <div className="header__account__info__username">
                                    Анатолий Ничведо
                                </div>
                                <div className="header__account__info__balance">
                                    <i className={`fa fa-usd`} aria-hidden="true"/>
                                    <span>13 500</span>
                                </div>
                            </div>
                            <div className="header__account__avatar">
                                <img src={NoAvatar} alt=""/>
                            </div>
                        </div>
                    </div>
                }
            </div>
        )
    }

}
