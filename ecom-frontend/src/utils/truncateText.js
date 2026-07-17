const truncateText = (text, charsLimit = 90) => {

    let textFinal = text;

    if(text?.length > charsLimit) {
        textFinal = text.slice(0,charsLimit) + "...";
    }

    return textFinal;
}

export default truncateText;