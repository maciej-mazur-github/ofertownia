$(document).ready(function() {
    registerTodaysWeatherSlider();
    registerAccordion();
});

const registerTodaysWeatherSlider = () => {
    let parentId = 'parent';
    let childContainerId = 'child-container';
    let childId = 'child';
    makeDraggable(parentId, childContainerId, childId);
    window.addEventListener('resize', (event) => {
        adjustDraggableDimensions(parentId, childContainerId, childId);
    });
}

const registerAccordion = () => {
    let size = longTermForecastSize;
    for (let i = 0; i < longTermForecastSize; i++) {
        let targetNodeId = "panelsStayOpen-collapse-" + i;
        let targetNode = document.getElementById(targetNodeId);
        let childId = 'child-' + i;
        let childContainerId = 'child-container-' + i;
        let parentId = 'parent-' + i;

        window.addEventListener('resize', (event) => {
            adjustDraggableDimensions(parentId, childContainerId, childId);
        });
        let classWatcher = new ClassWatcher(targetNode, parentId, childContainerId, childId, 'show', makeDraggable);
    }
}

function makeDraggable(parentId, childContainerId, childId) {
    jQuery(`#${childId}`).draggable({
        axis: 'x',
        cursor: "move",
        containment: `#${childContainerId}`
    });
    adjustDraggableDimensions(parentId, childContainerId, childId);
}

function adjustDraggableDimensions(parentId, childContainerId, childId) {
    let parent = document.getElementById(parentId);
    let child = document.getElementById(childId);
    let childContainer = document.getElementById(childContainerId);
    let parentWidth = parent.clientWidth;
    let childWidth = child.clientWidth;
    let parentChildWidthDiff = (childWidth - parentWidth) > 0 ? (childWidth - parentWidth) : 0;
    let childLeft = parentChildWidthDiff + 10;
    let childContainerWidth = childWidth + parentChildWidthDiff + 10;
    let childContainerLeft = -1 * (parentChildWidthDiff + 10);

    child.style.left = childLeft + "px";
    childContainer.style.width = childContainerWidth + "px";
    childContainer.style.left = childContainerLeft + "px";
}

class ClassWatcher {
    constructor(targetNode, parentId, childContainerId, childId, classToWatch, classAddedCallback) {
        this.targetNode = targetNode;
        this.parentId = parentId;
        this.childContainerId = childContainerId;
        this.childId = childId;
        this.classToWatch = classToWatch;
        this.classAddedCallback = classAddedCallback;
        this.observer = null;
        this.lastClassState = targetNode.classList.contains(this.classToWatch);

        this.init();
    }

    init() {
        this.observer = new MutationObserver(this.mutationCallback);
        this.observe();
    }

    observe() {
        this.observer.observe(this.targetNode, {attributes: true});
    }

    disconnect() {
        this.observer.disconnect();
    }

    mutationCallback = mutationList => {
        for (let mutation of mutationList) {
            if (mutation.type === 'attributes' && mutation.attributeName === 'class') {
                let currentClassState = mutation.target.classList.contains(this.classToWatch);
                if (this.lastClassState !== currentClassState) {
                    this.lastClassState = currentClassState;
                    if (currentClassState) {
                        this.classAddedCallback(this.parentId, this.childContainerId, this.childId);
                    }
                }
            }
        }
    };
}