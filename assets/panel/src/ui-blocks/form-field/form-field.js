import React, {Component} from 'react';
import './form-field.css';


export const FormFieldTypes = {
    INPUT: 'input',
    BUTTON: 'button',
};


class FormField extends Component {

    static defaultProps = {
        type: FormFieldTypes.INPUT,
    };


    render() {

        switch (this.props.type) {

            case FormFieldTypes.INPUT:
                return (
                    <div>Input</div>
                );

            case FormFieldTypes.BUTTON:
                return (
                    <div>Button <i className="fa fa-snowflake-o"/></div>
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
