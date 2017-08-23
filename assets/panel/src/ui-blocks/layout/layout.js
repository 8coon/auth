import React, {Component} from 'react';

// Debounce
import debounce from '../../utils/debounce';


export const LayoutSize = {
    handheld: 's',
    tablet: 'm',
    desktop: 'l',
};


export default class Layout extends Component {

    static defaultProps = {
        onReflow: () => {},
    };

    constructor(props) {
        super(props);
        this.state = {size: LayoutSize.desktop};

        this.onResize = this.onResize.bind(this);
        this.onDebouncedResize = this.onDebouncedResize.bind(this);
    }

    onResize() {
        debounce(this.onDebouncedResize);
    }

    onDebouncedResize() {
        const width = window.innerWidth;
        let newSize = this.state.size;

        if (width <= 725) {
            newSize = LayoutSize.handheld;
        } else if (width <= 1025) {
            newSize = LayoutSize.tablet;
        } else {
            newSize = LayoutSize.desktop;
        }

        if (newSize !== this.state.size) {
            this.setState({size: newSize});
            this.props.onReflow();
        }
    }

    componentDidMount() {
        window.addEventListener('resize', this.onResize);
        this.onDebouncedResize();
    }

    componentWillUnmount() {
        window.removeEventListener('resize', this.onResize);
    }

    render() {
        const children = React.Children.map(this.props.children, child => {
            return React.cloneElement(child, {...child.props, size: this.state.size});
        });

        return (<div style={{height: /*'100%'*/ null}}>{children}</div>);
    }

}
