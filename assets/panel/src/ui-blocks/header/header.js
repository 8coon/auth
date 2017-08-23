import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Link} from 'react-router-dom';
import './header.css';

// Assets
import Logo from '../../assets/logo.png';

// Sitemap
import Sitemap from '../../sitemap';

// Size
import {LayoutSize} from '../layout/layout';

// UI-Blocks
import Dropdown from '../dropdown/dropdown';
import Input from '../input/input';
import Avatar from '../avatar/avatar';

// Services
import Status from '../../services/status';

// Models
import User from 'minecraftshire-jsapi/src/models/User/User';


export default class Header extends Component {

    static contextTypes = {
        router: PropTypes.object,
        model: PropTypes.object,
    };

    static defaultProps = {
        onBurgerClick: () => {},
    };

    constructor(props) {
        super(props);
        this.state = {search: false, user: new User()};

        this.onLogoClick = this.onLogoClick.bind(this);
        this.onSearchKeyPress = this.onSearchKeyPress.bind(this);
        this.onSearchFocus = this.onSearchFocus.bind(this);
        this.onSearchBlur = this.onSearchBlur.bind(this);
        this.onAccountClick = this.onAccountClick.bind(this);
        this.onStatusChange = this.onStatusChange.bind(this);
    }

    componentDidMount() {
        Status.$on(Status.EVT_STATUS_CHANGE, this.onStatusChange);
    }

    componentWillUnmount() {
        Status.$off(Status.EVT_STATUS_CHANGE, this.onStatusChange);
    }

    onStatusChange(evt) {
        this.setState({user: evt.details});
    }

    onLogoClick() {
        this.context.router.history.push(Sitemap.root);
    }

    onSearchKeyPress(evt) {
        if (evt.keyCode === 13) {

        }
    }

    onSearchFocus() {
        this.setState({search: true});
    }

    onSearchBlur() {
        this.setState({search: false});
        console.log('SEARCH UNFOCUS');
    }

    onAccountClick() {

    }

    render() {
        const user = this.context.model.user;

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
                                <Dropdown controlText={user.get('username')}
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
                            <div className="header__account__wrapper">
                                <div className="header__account__info"
                                     onClick={this.onAccountClick}>
                                    <div className="header__account__info__username">
                                        {user.get('username')}
                                    </div>
                                    <div className="header__account__info__balance">
                                        <i className={`fa fa-usd`} aria-hidden="true"/>
                                        <span>{user.get('freeBalance')}</span>
                                    </div>
                                </div>
                                <Avatar url={user.getAvatarFullUrl()}
                                        onClick={this.onAccountClick}/>
                            </div>
                            <div className="header__account__logout" style={{display: 'none'}}>
                                <Link to={Sitemap.root}>Выход</Link>
                            </div>
                        </div>
                    </div>
                }
            </div>
        )
    }

}
