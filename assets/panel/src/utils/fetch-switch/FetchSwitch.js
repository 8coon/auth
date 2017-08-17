import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {matchPath} from 'react-router-dom';

// Utils
import TimeoutPromise from '../TimeoutPromise';


export default class FetchSwitch extends Component {

    static contextTypes = {
        router: PropTypes.object,
        model: PropTypes.object,
    };

    static childContextTypes = {
        model: PropTypes.object,
    };

    static defaultProps = {
        onNavigate: () => Promise.resolve(),
    };

    constructor(props) {
        super(props);

        this.state = {currentComponent: null, model: {}};
        this.futureComponent = {props: {path: 1}};
        this.navigating = false;
    }

    getChildContext() {
        return {model: this.state.model};
    }

    navigate(models, props) {
        this.navigating = false;

        this.setState({
            currentComponent: React.cloneElement(this.futureComponent, props),
            model: Object.assign(this.state.model, ...models),
        });
    }

    render() {
        const route = this.context.router.route;
        const location = this.props.location || route.location;
        let match = null;
        let child = null;

        React.Children.forEach(this.props.children, (element) => {
            if (!React.isValidElement(element)) {
                return;
            }

            const elementProps = element.props;
            const pathProp = elementProps.path;

            const exact = elementProps.exact;
            const strict = elementProps.strict;
            const from = elementProps.from;
            const path = pathProp || from;

            if (match == null) {
                child = element;
                match = path ? matchPath(location.pathname, {path, exact, strict}) : route.match;
            }
        });

        this.futureComponent = match ? child : null;

        if (!this.navigating && (!this.state.currentComponent ||
                this.futureComponent.props.path !== this.state.currentComponent.props.path)) {
            this.navigating = true;

            const component = this.futureComponent && this.futureComponent.props.component;
            const promise = component && component.prepare && component.prepare();

            if (promise && promise.then) {
                Promise.all([promise, this.props.onNavigate()])
                    .then(models => this.navigate(models, {location: location, computedMatch: match}));
            } else {
                Promise.all([this.props.onNavigate(), new TimeoutPromise(0)])
                    .then(models => this.navigate(models, {location: location, computedMatch: match}));
            }
        }

        return this.state.currentComponent;
    }

}
