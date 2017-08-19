import React, {Component} from 'react';
import './form-field.css';

// UI-blocks
import Input from '../input/input';
import Button from '../button/button';
import Tooltip from '../tooltip/tooltip';


export const FormFieldTypes = {
    INPUT: 'input',
    BUTTON: 'button',
};

export const FormFieldModes = {
    NORMAL: 'normal',
    FLEXIBLE: 'flexible',
};


class FormField extends Component {

    static defaultProps = {
        type: FormFieldTypes.INPUT,
        placeholder: '',
        text: '',
        password: false,
        mode: FormFieldModes.NORMAL,
        validator: () => true,

        onAction: () => {},
        onFocus: () => {},
        onBlur: () => {},
    };

    constructor(props) {
        super(props);
        this.state = { error: null, tooltipVisible: true };

        this.onChange = this.onChange.bind(this);
    }

    onChange(evt) {
        this.setState({ error: null });
    }

    tooltip() {
        return (
            <Tooltip ref="tooltip"
                     opacity={this.state.error !== null ? 1 : 0}
                     visible={this.state.tooltipVisible}>
                {this.state.error}
            </Tooltip>
        );
    }

    validate(result = null) {
        if (result === null) {
            result = this.props.validator(this.refs.text.state.text);
        }

        if (result === true) {
            this.setState({ error: null });
            this.refs.text.validate(true);
        } else {
            this.setState({ error: result });
            this.refs.text.validate(false);
        }

        return result;
    }

    toggleTooltip(visible) {
        this.setState({ tooltipVisible: visible });
    }

    getText() {
        return this.refs.text.state.text;
    }

    render() {
        switch (this.props.type) {

            case FormFieldTypes.INPUT:
                return (
                    <span className={`form-field_${this.props.mode}`}>
                        <Input
                            ref="text"
                            placeholder={this.props.placeholder}
                            text={this.props.text}
                            password={this.props.password}
                            onChange={this.onChange}
                            onFocus={this.props.onFocus}
                            onBlur={this.props.onBlur}/>
                        {this.tooltip()}
                    </span>
                );

            case FormFieldTypes.BUTTON:
                return (
                    <span className={`form-field_${this.props.mode}`}>
                        <Button
                            text={this.props.text}
                            onClick={this.props.onAction}/>
                        {this.tooltip()}
                    </span>
                );

            default:
                return (
                    <div style={{"font-weight": 700, color: "red"}}>
                        Unknown field type: {this.props.type}!
                    </div>
                );
        }
    }

}


export default FormField;
