import React, {Component} from 'react';
import './button.css';


export const ButtonMode = {
    FORM: 'form',
};


export default class Button extends Component {

    static defaultProps = {
        mode: ButtonMode.FORM,
        text: 'Button',
        onClick: () => {},
    };

    render() {
        return (
            <div className="button">
                <button type="button" onClick={this.props.onClick}>
                    {this.props.text}
                </button>
            </div>
        )
    }

}
