import React, {Component} from 'react';
import './layer-popup.css';


const Z_INDEX_BASE = 6000;


export default class LayerPopup extends Component {

    static components = [];
    static windows = [];

    constructor(props) {
        super(props);
        this.state = {visible: false}
    }

    static openWindow(window) {
        LayerPopup.components.push(window);
        LayerPopup.toggleComponents(true);
    }

    static closeWindow(window) {
        LayerPopup.components.splice(LayerPopup.components.indexOf(window), 1);
        LayerPopup.toggleComponents(LayerPopup.components.length === 0 ? false : void 0);
    }

    static toggleComponents(visible) {
        LayerPopup.components.forEach(component =>
                component.setState({visible: visible === void 0 ? component.state.visible : visible}));
    }

    componentDidMount() {
        LayerPopup.components.push(this);
    }

    componentWillUnmount() {
        LayerPopup.components.splice(LayerPopup.components.indexOf(this), 1);
    }

    renderWindows() {
        return LayerPopup.windows.map((window, idx) => {
            return (
                <div className="layer-popup__window"
                     key={idx}
                     style={{zIndex: Z_INDEX_BASE + idx}}>
                    <div className="layer-popup__window__title-bar">
                        <div className="layer-popup__window__close-button"
                             onClick={() => LayerPopup.closeWindow(window)}>
                            <i className="fa fa-times" aria-hidden="true"/>
                        </div>
                    </div>

                    <div className="layer-popup__window__content">
                        {window}
                    </div>
                </div>
            )
        });
    }

    render() {
        return (
            <div className={`layer-popup ${this.state.visible ? '' : 'layer-popup_hidden'}`}>
                <div className="layer-popup__background"/>
                {this.renderWindows()}
            </div>
        )
    }

}
