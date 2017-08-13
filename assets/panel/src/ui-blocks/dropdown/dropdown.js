import React, {Component} from 'react';
import './dropdown.css';

// Polyfills
import {getEventPath} from '../../utils/polyfills';


export class DropdownControl extends Component {

    constructor(props) {
        super(props);

        this.onClick = this.onClick.bind(this);
    }

    onClick() {
        this.props.parent.toggle();
    }

    render() {
        return (
            <div className={`dropdown-control dropdown-toggle-stopper
                        ${this.props.transparent ? 'dropdown-control_transparent' : ''}
                        ${this.props.toggled ? 'dropdown-control_toggled' : ''}`}
                 onClick={this.onClick}>
                {
                    this.props.hasLeftArrow && (
                        <div className="dropdown-control__arrow">
                            <i className="fa fa-caret-down" aria-hidden="true"/>
                        </div>
                    )}
                {
                    this.props.hasText && (
                    <div className={`dropdown-control__text
                            ${this.props.hasRightArrow ? 'dropdown-control__text_right-arrow' : ''}
                            ${this.props.hasLeftArrow ? 'dropdown-control__text_left-arrow' : ''}`}>
                        {this.props.text}
                    </div>
                )}
                {
                    this.props.hasRightArrow && (
                    <div className="dropdown-control__arrow">
                        <i className="fa fa-caret-down" aria-hidden="true"/>
                    </div>
                )}
            </div>
        )
    }

}


export default class Dropdown extends Component {

    static defaultProps = {
        control: (<DropdownControl/>),
        hasText: true,
        hasRightArrow: true,
        hasLeftArrow: false,
        transparent: false,
        controlText: '',
    };

    constructor(props) {
        super(props);

        this.state = {toggled: false, animated: false};
        this.onOutsideClick = this.onOutsideClick.bind(this);
    }

    toggle(value) {
        const newToggled = (value === void 0) ? !this.state.toggled : value;

        if (newToggled !== this.state.toggled) {
            this.setState({toggled: newToggled, animated: false});
            window.setTimeout(() => this.setState({animated: true}), 1);
        }
    }

    onOutsideClick(evt) {
        if (!getEventPath(evt).some(el => el.classList && el.classList.contains('dropdown-toggle-stopper'))) {
            this.toggle(false);
        }
    }

    componentDidMount() {
        document.addEventListener('click', this.onOutsideClick);
    }

    componentWillUnmount() {
        document.removeEventListener('click', this.onOutsideClick);
    }

    render() {
        return (
            <div>
                {React.cloneElement(this.props.control, {
                    ...this.props.control.props,
                    ...this.props,
                    parent: this,
                    text: this.props.controlText,
                    toggled: this.state.toggled,
                })}

                <div className="dropdown__items"
                     ref="items"
                     style={{
                         display: this.state.toggled ? 'block' : 'none',
                         opacity: this.state.animated ? 1 : 0,
                         marginTop: this.state.animated ? 0 : '-100%',
                     }}>
                    {this.props.children}
                </div>
            </div>
        )
    }

}
