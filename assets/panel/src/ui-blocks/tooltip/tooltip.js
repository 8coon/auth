import React, {Component} from 'react';
import './tooltip.css';


export default class Tooltip extends Component {

    defaultProps = {
        visible: false,
        opacity: 1,
    };

    render() {
        if (!this.props.visible) {
            return (<span/>);
        }

        return (
            <span className="tooltip" style={{opacity: this.props.opacity}}>
                <span className="tooltip__arrow"/>
                <span className="tooltip__text">
                    {this.props.children}
                </span>
            </span>
        )
    }

}
