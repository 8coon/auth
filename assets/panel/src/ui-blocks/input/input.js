import React, {Component} from 'react';
import './input.css';


export const InputState = {
    INITIAL: 'initial',
    VALID: 'valid',
    INVALID: 'invalid',
};


export default class Input extends Component {

    static defaultProps = {
        text: '',
        placeholder: '',
        password: false,

        onChange: () => {},
        onFocus: () => {},
        onBlur: () => {},
    };

    constructor(props) {
        super(props);

        this.state = {
            text: this.props.text,
            state: InputState.INITIAL,
        };

        this.onChange = this.onChange.bind(this);
    }

    onChange(evt) {
        this.setState({ text: evt.target.value, state: InputState.INITIAL });
        this.props.onChange(evt);
    }

    validate(isValid) {
        this.setState({ state: isValid ? InputState.VALID : InputState.INVALID });
    }

    render() {
        return (
            <div className={`input input_${this.state.state.toLowerCase()}`}>
                <input
                    value={this.state.text}
                    placeholder={this.props.placeholder}
                    type={this.props.password ? 'password' : 'text'}
                    style={this.state.state === InputState.INVALID ? {animation: `input-error 0.3s`} : {}}
                    onChange={this.onChange}
                    onFocus={this.props.onFocus}
                    onBlur={this.props.onBlur}/>
            </div>
        )
    }

}
