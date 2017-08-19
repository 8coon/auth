import React, {Component} from 'react';
import './delimiter.css';


export default class Delimiter extends Component {

    static defaultProps = {
        text: 'Разделитель',
    };

    render() {
        return (
            <div className="delimiter">
                <span className="delimiter__text">
                    {this.props.text}
                </span>
            </div>
        )
    }

}
