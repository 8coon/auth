import React, {Component} from 'react';
import './application.css';


class Application extends Component {

    constructor(props) {
        super(props);
        this.state = {name: 'application'};
    }

    componentDidMount() {
        this.setState({name: 'application lol'});
    }

    componentWillUnmount() {
    }

    render() {
        return (
            <div className={this.state.name}>
                <h1>It Works!</h1>
            </div>
        )
    };

}


export default Application;
