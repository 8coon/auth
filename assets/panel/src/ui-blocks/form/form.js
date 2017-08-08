import React, {Component} from 'react';
import './form.css';


class Form extends Component {

    render() {
        return (
            <div className="form">
                {this.props.children}
            </div>
        )
    }

}


export default Form;
