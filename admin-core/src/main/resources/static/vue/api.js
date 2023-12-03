
function requestGet(url){
    // 发送删除请求
    axios.get(url).then(r => {
        if (r.data.code === 200) {
            location.reload();
        }
    }).catch(error => {
        console.error("请求失败：", error);
        ELEMENT.Message({
            showClose: true,
            message: "请求失败，请稍后重试",
            type: 'error'
        });
    })
}

function requestPost(url,data){
    axios.post(url, data).then(r => {
        if (r.data.code === 200) {
            location.reload();
        }else {
            ELEMENT.Message({
                showClose: true,
                message: r.data.msg,
                type: 'error'
            });
        }
    }).catch(error => {
        console.error("请求失败：", error);
        ELEMENT.Message({
            showClose: true,
            message: "请求失败，请稍后重试",
            type: 'error'
        });
    })
}
