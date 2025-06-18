window.addEventListener("DOMContentLoaded", function () {
    // 전체 체크 토글
    const chkAlls = document.getElementsByClassName("check-all");
    for (const el of chkAlls) {
        el.addEventListener("click", function () {
            const { targetName } = this.dataset;
            const chks = document.getElementsByName(targetName);
            for (const chk of chks) {
                chk.checked = this.checked;
            }
        });
    }

    // 공통 처리 버튼
    const processFormButtons = document.getElementsByClassName("process-form");
    for (const el of processFormButtons) {
        el.addEventListener("click", function () {
            const method = this.classList.contains("delete") ? "DELETE" : "PATCH";
            const { formName } = this.dataset;
            const formEl = document.forms[formName];

            if (!formEl) {
                console.error("Form not found:", formName);
                return;
            }

            // _method input이 없으면 생성
            let methodInput = formEl.querySelector("input[name=_method]");
            if (!methodInput) {
                methodInput = document.createElement("input");
                methodInput.type = "hidden";
                methodInput.name = "_method";
                formEl.appendChild(methodInput);
            }
            methodInput.value = method;

            // 확인 후 전송
            if (confirm("정말 처리하겠습니까?")) {
                formEl.submit();
            }
        });
    }
});