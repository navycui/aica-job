export const checkValidity = (formId: string) => {
  const form = document.getElementById(formId)
  let valid = true
  let first = false

  if (form) {
    const elementList = form.getElementsByTagName('div')
    if (elementList) {
      for (let i = 0; i < elementList.length; i++) {
        let div = elementList.item(i);

        if (div?.classList.contains('MuiInputBase-root')){
          let kind = 'input'
          div.classList.forEach(v => {
            if (v.includes('MuiInputBase-multiline')) {
              kind = 'multiline'
              return false
            }else if (v.includes('MuiSelect-root')){
              kind = 'select'
              return false
            }
          })

          const selector = kind == 'multiline'? 'textarea' : 'input'
          const input = div.querySelector(selector)
          if (input?.required) {
            if (input.validity.valueMissing){
              // 처음 값없는 필드로 이동.
              if (!first) {
                input.focus()
                input.scrollIntoView({ behavior: 'smooth',block: "end" })
                first = true
              }

              div.classList.add('Mui-error')
              if (!div.parentElement?.querySelector('p')) {
                let p = document.createElement('p');
                p.innerText = `${input.name? input.name : '내용'}를 입력해주세요.`
                p.style['fontSize'] = '0.75rem'
                p.style['color'] = 'red'
                p.style['margin'] = '10px 0 0 0'
                if (selector == 'textarea') {
                  p.style['position'] = 'absolute'
                  p.style['bottom'] = '-18px'
                }
                div.parentElement?.append(p)
              }

              if (kind == 'select'){
                div.addEventListener('DOMNodeInserted', (event) => {
                  const p = div?.parentElement?.querySelector('p')
                  if (p) div?.parentElement?.removeChild(p)
                }, false)
              }else {
                input.addEventListener('change', (event) => {
                  const p = div?.parentElement?.querySelector('p')
                  if (p) div?.parentElement?.removeChild(p)
                })
              }
              valid = false
            }
          }
        }
      }
    }
  }

  return valid
}