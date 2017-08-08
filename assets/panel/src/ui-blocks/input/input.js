import React, {Component} from 'react';
import './input.css';


export const InputState = {
    INITIAL: 'initial',
    VALID: 'valid',
    INVALID: 'invalid',
};


export default class Input extends Component {

    static defaultProps = {
        placeholder: '',
        password: false,
    };

    constructor(props) {
        super(props);

        this.state = {
            text: '',
            state: InputState.INITIAL,
        };

        this.onChange = this.onChange.bind(this);
    }

    onChange(evt) {
        this.setState({ text: evt.target.value });
    }

    render() {
        console.log(this);
        return (
            <div className="input">
                <input
                    ref="text"
                    value={this.state.text}
                    placeholder={this.props.placeholder}
                    type={this.props.password ? 'password' : 'text'}
                    onChange={this.onChange}/>
            </div>
        )
    }

}
