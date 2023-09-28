import React, {Dispatch, SetStateAction, useEffect, useState} from 'react'
import {Box, IconButton, Stack, Typography} from "@mui/material";
import {EquipmentCategoryService} from "~/service/EquipmentMgt/EquipmentCategoryService";
import {useEquipmentClassifyStore} from "~/store/EquipmentMgt/EquipmentClassifyStore";
import {
  EquipmentClassifyData,
  EquipmentClassifyRequest,
  EquipmentClassifyRowData
} from "~/service/Model";
import {TreeItem, TreeItemContentProps, TreeView, useTreeItem} from "@mui/lab";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import {GridCellEditCommitParams, GridColumns} from "@mui/x-data-grid";
import {CustomButton, CustomIconButton} from "shared/components/ButtonComponents";
import {HorizontalInterval, SubContents, TitleContents, VerticalInterval} from "shared/components/LayoutComponents";
import DataTable from "shared/components/CustomDataGride";
import {GridSelectionModel} from "@mui/x-data-grid/models/gridSelectionModel";
import {GridRowOrderChangeParams} from "@mui/x-data-grid-pro";
import clsx from "clsx";
import {Color} from "shared/components/StyleUtils";
import {useMutation} from "react-query";
import {Icons} from "shared/components/IconContainer";
import {useGlobalModalStore} from "~/store/GlobalModalStore";
import * as R from "ramda";

/* 장비 분류 관리 */
const EquipmentCategory = () => {
  const category = EquipmentCategoryService.getRoot()
  const equipmentClassify = useEquipmentClassifyStore()
  const [editRows, setEditRow] = useState<EquipmentClassifyRowData[]>([])
  const {addModal} = useGlobalModalStore();

  useEffect(() => {
    if (!category.isLoading && !category.isFetching) {
      if (!!category.data) {
        equipmentClassify.setCategoryFlatten(category.data.list)
        const hierarchy = dataHierarchy(R.pipe(JSON.stringify, JSON.parse)(category.data.list))

        equipmentClassify.setCurrentCategory(hierarchy)
        if (editRows.length === 0) {
          const rowData = hierarchy.child?.map((m, i) => {
            return {
              id: m.eqpmnClId,
              eqpmnClId: m.eqpmnClId,
              ordr: m.ordr,
              eqpmnClNm: m.eqpmnClNm,
              useAt: m.useAt ? "사용" : "미사용",
            }
          }).sort((a, b) => a.ordr - b.ordr)

          if (rowData) setEditRow(rowData)
        }
      }
    }
  }, [category.data, category.isLoading, category.isFetching]);

  return <TitleContents title={"장비분류관리"}>
    <Stack
      flexDirection={"row"}
      justifyContent={"space-between"}
    >
      <LeftTreeView
        onClickTree={(equipment: EquipmentClassifyData) => {
          equipmentClassify.setCurrentCategory(equipment)
          if (equipment.child) {
            const data = equipment.child.map((m, i) => {
              return {
                id: m.eqpmnClId,
                eqpmnClId: m.eqpmnClId,
                ordr: m.ordr,
                eqpmnClNm: m.eqpmnClNm,
                useAt: m.useAt ? "사용" : "미사용",
              }
            }).sort((a, b) => a.ordr - b.ordr)

            setEditRow(data)
          } else {
            setEditRow([])
          }
        }}/>

      <RightTableView
        tableRows={editRows}
        isLoading={category.isLoading || category.isFetching}
        setTableRows={setEditRow}
        onPresSave={async (parentId: string, req: EquipmentClassifyRequest[]) => {
          try {
            const editEquipment = await EquipmentCategoryService.putCategory(parentId, req)
            if (editEquipment.success) {
              addModal({open: true, isDist: true, type: 'confirm', content: "저장 완료."})
            }

            const newData = editEquipment.list.filter(f => {
              return !equipmentClassify.categoryFlatten.find(d => d.eqpmnClId === f.eqpmnClId)
            })
            // 기존 값은 덮어 쓰고 새로운 데이터 추가
            const edit = [
              ...equipmentClassify.categoryFlatten.map(m => {
                const findData = editEquipment.list.find(f => f.eqpmnClId === m.eqpmnClId)
                return findData ? findData : m
              }),
              ...newData
            ]
            const hierarchy = dataHierarchy(R.pipe(JSON.stringify, JSON.parse)(edit))

            const classify = findCategory(parentId, hierarchy)

            equipmentClassify.setCategoryFlatten(edit)
            if (classify) {
              equipmentClassify.setCurrentCategory(classify)

              const data = classify.child?.map((m, i) => {
                return {
                  id: m.eqpmnClId,
                  eqpmnClId: m.eqpmnClId,
                  ordr: m.ordr,
                  eqpmnClNm: m.eqpmnClNm,
                  useAt: m.useAt ? "사용" : "미사용",
                }
              }).sort((a, b) => a.ordr - b.ordr)

              if (data) setEditRow(data)
            }
          } catch (e) {
            console.warn(e)
          }
        }}
        onPressDelete={async (parentId: string, req: EquipmentClassifyRequest[]) => {
          try {
            const updateCategory = await EquipmentCategoryService.deleteCategory(parentId, req)

            if (!updateCategory.success) return;

            addModal({open: true, isDist: true, content: "삭제 완료."})
            const deleteIds = req.flatMap(m => m.eqpmnClId)

            const updateData = equipmentClassify.categoryFlatten.filter(f => {
              return !deleteIds.includes(f.eqpmnClId)
            })
            equipmentClassify.setCategoryFlatten(updateData)

            const hierarchy = dataHierarchy(R.pipe(JSON.stringify, JSON.parse)(updateData))
            const classify = findCategory(parentId, hierarchy)

            if (classify) {
              equipmentClassify.setCurrentCategory(classify)
              classify.child?.forEach(e => {
                const find = updateCategory.list.find(f => f.eqpmnClId === e.eqpmnClId)
                if (find) {
                  e.ordr = find.ordr
                }
              })

              const data = classify.child?.map((m, i) => {
                return {
                  id: m.eqpmnClId,
                  eqpmnClId: m.eqpmnClId,
                  ordr: m.ordr,
                  eqpmnClNm: m.eqpmnClNm,
                  useAt: m.useAt ? "사용" : "미사용",
                }
              }).sort((a, b) => a.ordr - b.ordr)

              if (data) setEditRow(data)
            }
          } catch (e) {
            console.warn(e)
          }
        }}
      />
    </Stack>
  </TitleContents>
}

const Item: React.FC<{
  depth: number
  equipment: EquipmentClassifyData,
  onClick: (equipment: EquipmentClassifyData) => void
}> = props => {
  // const isDepth = props.equipment.child.length > 0
  const isDepth = props.depth < 2
  return <>
    {
      isDepth && <TreeItem
            ContentComponent={CustomContent}
            nodeId={props.equipment.eqpmnClId}
            label={
              <div onClick={() => props.onClick(props.equipment)}>
                {props.equipment.eqpmnClNm}
              </div>
            }>
        {
          props.equipment.child?.map((m, i) => {
            return <Item key={i} depth={props.depth + 1} equipment={m} onClick={props.onClick}/>
          })
        }
        </TreeItem>
    }
  </>
}

const LeftTreeView: React.FC<{
  onClickTree: (equipment: EquipmentClassifyData) => void
}> = props => {
  const {categoryFlatten} = useEquipmentClassifyStore();
  const root = dataHierarchy(R.pipe(JSON.stringify, JSON.parse)(categoryFlatten))

  return <TreeView
    defaultCollapseIcon={<ExpandMoreIcon/>}
    defaultExpandIcon={<ChevronRightIcon/>}
    defaultExpanded={['root']}
    sx={{overflowY: 'auto', minWidth: '180px', marginRight: "10px", border: "solid 1px", borderColor: Color.divider}}>
    <TreeItem
      ContentComponent={CustomContent}
      nodeId={"root"}
      label={
        <div onClick={() => props.onClickTree(root!)}>
          {"장비분류"}
        </div>
      }>
      {
        root?.child && root.child.map((m, i) => {
          return <Item key={i} depth={1} equipment={m} onClick={props.onClickTree}/>
        })
      }
    </TreeItem>
  </TreeView>
}

const RightTableView: React.FC<{
  tableRows: EquipmentClassifyRowData[]
  setTableRows: Dispatch<SetStateAction<EquipmentClassifyRowData[]>>
  isLoading: boolean
  onPresSave: (parentId: string, req: EquipmentClassifyRequest[]) => void
  onPressDelete: (parentId: string, req: EquipmentClassifyRequest[]) => void
}> = props => {
  const [removeData, setRemoveData] = useState<EquipmentClassifyData[]>([])
  const {currentCategory, tableRows, setRow} = useEquipmentClassifyStore()
  const [count, setCount] = useState<number>(0)
  const {addModal} = useGlobalModalStore();

  const columns: GridColumns = [
    {field: 'id', hide: true},
    {field: 'eqpmnClId', hide: true},
    {field: 'ordr', headerName: '순서', flex: 2, headerAlign: 'center', align:'center'},
    {field: 'eqpmnClNm', headerName: '분류명', flex: 10, headerAlign: 'center', editable: true, align:'center'},
    {
      field: 'useAt',
      headerName: '사용여부',
      align: 'center',
      flex: 2,
      headerAlign: 'center',
      type: "singleSelect",
      valueOptions: ["사용", "미사용"],
      editable: true
    },
  ]

  // if (props.tableRows.length === 0) return <></>

  return <SubContents
    title={"분류"}
    maxHeight={"100%"}
    rightContent={
      <Stack flexDirection={"row"}>
        <CustomIconButton
          icon={Icons.Trash}
          onClick={() => {
            if (count === 0) {
              addModal({open: true, isDist: true, type: 'normal', content: '삭제 불가.'})
              return
            }
            if (removeData.length >= 0) {
              const req: EquipmentClassifyRequest[] = removeData.map(m => {
                return {
                  eqpmnClId: m.eqpmnClId,
                  eqpmnLclasId: m.eqpmnLclasId
                }
              })
              props.onPressDelete(currentCategory?.eqpmnClId!, req)
              setRemoveData([])
            }
          }}/>

        <CustomIconButton
          icon={Icons.Plus}
          onClick={() => {
            props.setTableRows([
              ...props.tableRows,
              {
                id: "temp_" + props.tableRows.length,
                eqpmnClId: "temp_" + props.tableRows.length,
                eqpmnClNm: "",
                useAt: "미사용",
                ordr: props.tableRows.length + 1
              }])
          }}/>
      </Stack>
    }>
    <Stack sx={{display: "flex", width: '100%'}} pb={"30px"}>
      <DataTable
        hideFooter rowReordering isCheckBox
        loading={props.isLoading}
        rows={props.tableRows}
        columns={columns}
        rowCount={props.tableRows.length}
        onSelectionModelChange={(selectionModel: GridSelectionModel) => {
          if (selectionModel!){
            setCount(selectionModel.length)
          }
          if (currentCategory?.child) {
            const selected = currentCategory.child.filter(f => {
              if (selectionModel.includes(f.eqpmnClId!))
                return f
            })
            if (!!selected)
              setRemoveData(selected)
          }
        }}
        onCellEditCommit={(params: GridCellEditCommitParams) => {
          props.setTableRows(props.tableRows.map(m => {
            if (m.eqpmnClId === params.id) {
              if (params.field === "eqpmnClNm") return {...m, eqpmnClNm: params.value}
              else if (params.field === "useAt") return {...m, useAt: params.value}
            }
            return m
          }))
        }}
        onRowOrderChange={(params: GridRowOrderChangeParams) => {
          if (props.tableRows) {
            const rowsClone = [...props.tableRows];
            const row = rowsClone.splice(params.oldIndex, 1)[0];
            rowsClone.splice(params.targetIndex, 0, row);
            props.setTableRows(rowsClone);
          }
        }}
      />

      <VerticalInterval size={"40px"}/>
      <Box style={{display: "flex", justifyContent: "end"}}>
        <CustomButton
          label={"저장"}
          onClick={() => {
            if (count === 0) {
              addModal({open: true, isDist: true, type: 'normal', content: '저장 불가.'})
              return
            }
            const req: EquipmentClassifyRequest[] = props.tableRows.filter(f => f.eqpmnClNm != "").map((m, i) => {
              return {
                eqpmnClId: m.eqpmnClId.includes("temp") ? undefined : m.eqpmnClId,
                eqpmnClNm: m.eqpmnClNm,
                ordr: i + 1,
                useAt: m.useAt === "사용",
                eqpmnLclasId: currentCategory?.eqpmnClId
              }
            });
            props.onPresSave(currentCategory?.eqpmnClId!, req)
          }}/>
      </Box>
    </Stack>
  </SubContents>
}

const findCategory = (id: string, dest: EquipmentClassifyData) => {
  if (dest.eqpmnClId === id) return dest;
  else {
    return dest.child?.find(f => {
      if (f.eqpmnClId === id) return f;
      else findCategory(id, f)
    })
  }
}

const dataHierarchy = (categorys: EquipmentClassifyData[]) => {
  const root = categorys.find(f => f.eqpmnLclasId === "ROOT")
  const map = categorys.reduce((a, b, i) => ({...a, [b.eqpmnClId]: i}), {});
  const hierarchy: EquipmentClassifyData = {...root!, eqpmnClId: 'ROOT', child: []}

  categorys.forEach((item) => {
    if (item.eqpmnClId != "ROOT") {
      if (item.eqpmnLclasId === "ROOT") {
        hierarchy.child?.push(item)
      } else if (item.eqpmnLclasId) {
        //@ts-ignore
        const obj = categorys[map[item.eqpmnLclasId]]
        if (!obj.child) obj.child = []
        obj.child.push(item)
        // obj.child.sort((a, b) => a.sortOrder - b.sortOrder)
      }
    }
  })

  return hierarchy
}

const CustomContent = React.forwardRef(function CustomContent(
  props: TreeItemContentProps,
  ref,
) {
  const {
    classes,
    className,
    label,
    nodeId,
    icon: iconProp,
    expansionIcon,
    displayIcon,
  } = props;

  const {
    disabled,
    expanded,
    selected,
    focused,
    handleExpansion,
    handleSelection,
    preventSelection,
  } = useTreeItem(nodeId);

  const icon = iconProp || expansionIcon || displayIcon;

  const handleMouseDown = (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
    preventSelection(event);
  };

  const handleExpansionClick = (
    event: React.MouseEvent<HTMLDivElement, MouseEvent>,
  ) => {
    handleExpansion(event);
  };

  const handleSelectionClick = (
    event: React.MouseEvent<HTMLDivElement, MouseEvent>,
  ) => {
    handleSelection(event);
  };

  return (
    // eslint-disable-next-line jsx-a11y/no-static-element-interactions
    <div
      className={clsx(className, classes.root, {
        [classes.expanded]: expanded,
        [classes.selected]: selected,
        [classes.focused]: focused,
        [classes.disabled]: disabled,
      })}
      onMouseDown={handleMouseDown}
      ref={ref as React.Ref<HTMLDivElement>}
    >
      {/* eslint-disable-next-line jsx-a11y/click-events-have-key-events,jsx-a11y/no-static-element-interactions */}
      <div onClick={handleExpansionClick} className={classes.iconContainer}>
        {icon}
      </div>
      <Typography
        onClick={handleSelectionClick}
        component="div"
        className={classes.label}
      >
        {label}
      </Typography>
    </div>
  );
});


export default EquipmentCategory;